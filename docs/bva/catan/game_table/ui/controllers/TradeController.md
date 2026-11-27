# Class TradeController

Classpath: `catan.game_table.ui.controllers.TradeController`

## `public class TradeController`

`TradeController` handle `TradeView` actions and renders the trade overlay. `GameTableController` opens the workflow, confirms who is viewing the table, and hosts or closes the overlay. `GameSession` creates and wires the match, supplies the active player, trading status, and active offers, and forwards trade commands to `PlayerTradeHelper` and `BankTradeHelper`. The helpers enforce trade rules and handle offer and inventory changes.

The controller tracks the current screen and the selected offer for a response. The `int` IDs in the sketch identify individual offer instances for the match. The planned `GameSession` trade interface supplies stable IDs and resolves them against current offers, so an expired or replaced ID cannot select another offer. Commands use the confirmed viewer as the actor. Action handlers run through the current screen's callbacks, which are retired when the screen is replaced or closed.

On a successful command, clear the selected offer, return to the overview, and ask `GameTableController` to refresh the table and overlay. If `GameSession` rejects a command with `InvalidTradeException`, show a trade error in `TradeView` and retain the current screen while it remains valid. An unavailable offer shows an error when acted on. If the selected response offer stops being pending, the next render clears the selection and falls back to the overview when trading is still open. Turn-end offer expiry belongs to `GameSession`.

### `public void open()`

If `GameSession` reports that trading is open, start on the overview for the active player and request an overlay refresh. If trading is closed, keep the overlay closed.

### `public Color requiredViewer()`

Return `Color.NONE` when the overlay is closed or trading has ended. Return the receiver of the current pending offer on the response screen. Return the active player's color on the overview, proposal form, or bank form, and when the selected response offer is no longer pending.

### `public Node render(Color viewer)`

Return an empty node when there is no required viewer. If `viewer` differs from `requiredViewer()`, render a handoff prompt for the required viewer without exposing trade details or handling trade actions. Once the viewer is confirmed, render the current screen through `TradeView` and handle its actions. 
The overview shows current offers involving the active player, using their IDs. Handle Request Response for unanswered outgoing offers and Finalize for accepted outgoing offers and incoming counteroffers. The response screen shows only the selected offer and its response controls.

### `private void onOpenProposal()`

From the overview, open the proposal form for the active player and request an overlay refresh.

### `private void onPropose(Color receiver, List<ResourceStack> give, List<ResourceStack> receive)`

Send a new offer to `GameSession` with the confirmed active player as proposer, `receiver` as recipient, `give` as offered resources, and `receive` as requested resources. `TradeOffer` takes requested resources before offered resources in its constructor.

### `private void onRequestResponse(int offerId)`

Look up the selected ID in `GameSession`. If it identifies an unanswered offer sent by the active player, select the offer, switch to the response screen, and request a handoff to the receiver through the overlay refresh. If the offer is unavailable or ineligible, show a trade error and keep the overview open.

### `private void onAccept(int offerId)`

Submit acceptance of the selected offer to `GameSession` as the confirmed receiver. The session routes to `acceptTrade()`, which records agreement. Finalization is a separate action for the active player.

### `private void onDecline(int offerId)`

Submit refusal of the selected offer to `GameSession` as the confirmed receiver. The session routes to `refuseTrade()`, which removes the unanswered offer.

### `private void onCounter(int offerId, List<ResourceStack> give, List<ResourceStack> receive)`

Submit the selected original offer and a new counteroffer to `GameSession`. The confirmed receiver becomes the counteroffer's proposer, and the original proposer becomes its receiver. `give` and `receive` are from the countering player's perspective. The session routes to `counterTrade()`, which replaces the original on success.

### `private void onFinalize(int offerId)`

Ask `GameSession` to finalize the selected offer as the confirmed active player. The session routes to `conductTrade()`. The active player can finalize their accepted proposal or a counteroffer addressed to them, including one they have yet to accept.

### `private void onOpenBankTrade()`

From the overview, open the bank trade form for the active player and request an overlay refresh.

### `private void onBankTrade(ResourceStack give, ResourceStack receive)`

Send the confirmed active player, `give`, and `receive` to `GameSession` for `tradeWithBank()`.

### `private void onClose()`

Clear the controller's screen and selected offer, then ask `GameTableController` to close the overlay. Any outstanding offers remain available if the player reopens trading on the same turn.
