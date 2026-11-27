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

## Boundary-Value Analysis

Combines cases and uses parameterized tests for repeated setups.

**Step 1: Inputs / outputs**

| Inputs                                                                                      | Outputs                                    |
|---------------------------------------------------------------------------------------------|--------------------------------------------|
| Trading open, current screen, active player, viewer, selected response offer, active offers | Required viewer, rendered node and actions |
| View action and its arguments (`offerId`, `receiver`, `give`, `receive`), model result      | Trade command, view update                 |

**Step 2: Data types**

| Variable                  | Type                                                                                                     |
|---------------------------|----------------------------------------------------------------------------------------------------------|
| Trading open              | Boolean                                                                                                  |
| Current screen            | Cases                                                                                                    |
| Active player             | `Color`                                                                                                  |
| Viewer                    | `Color`                                                                                                  |
| Selected response offer   | Cases                                                                                                    |
| Active offers             | Collection of offers keyed by ID                                                                         |
| View action               | Cases                                                                                                    |
| `offerId`                 | `int` used as a search key                                                                               |
| `receiver`                | `Color`                                                                                                  |
| `give`, `receive`         | Pair of `List<ResourceStack>` values for player trades or pair of `ResourceStack` values for bank trades |
| Model result              | Cases                                                                                                    |
| Required viewer           | `Color`                                                                                                  |
| Rendered node and actions | `Node` and its view callbacks                                                                            |
| Trade command             | Model call with actor and trade arguments                                                                |
| View update               | Cases                                                                                                    |

**Step 3: Concrete values**

| Variable                  | Values                                                                                                                                                                                                       |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Trading open              | `true`, `false`                                                                                                                                                                                              |
| Current screen            | Closed, overview, proposal form, response, bank form                                                                                                                                                         |
| Active player             | `Color.RED` in the test fixtures                                                                                                                                                                             |
| Viewer                    | Red for the active player's screens, Red before the handoff to Blue, Blue after the handoff                                                                                                                  |
| Selected response offer   | No selection, selected offer still pending, selected offer no longer pending                                                                                                                                 |
| Active offers             | Zero, one, or multiple offers, including two with identical terms and distinct IDs. Offer cases include unanswered outgoing, accepted outgoing, and unanswered incoming counteroffers                        |
| View action               | Open proposal, propose, request response, accept, decline, counter, finalize, open bank trade, bank trade, close, or invoke a retired callback                                                               |
| `offerId`                 | `42` matches one of two offers with identical terms, `42` is missing with two other offers still active, or `42` matches an offer that has already been accepted. `57` identifies a replacement counteroffer |
| `receiver`                | `Color.BLUE` for Red's proposal                                                                                                                                                                              |
| `give`, `receive`         | Different lists for a proposal or counteroffer, and different stacks for a bank trade. For example, `[1 brick, 1 lumber]` and `[1 ore]`, or `4 brick` and `1 ore`                                            |
| Model result              | Success or `InvalidTradeException`                                                                                                                                                                           |
| Required viewer           | `Color.NONE` when the overlay is closed or trading has ended, `Color.RED` for the active player's screens, or `Color.BLUE` for the pending response                                                          |
| Rendered node and actions | Empty node or handoff prompt without trade actions, or the current screen with its actions                                                                                                                   |
| Trade command             | Propose, accept, refuse, counter, finalize, or bank trade with the confirmed actor and corresponding arguments. Other actions may send no trade command                                                      |
| View update               | Refresh the overlay for a screen change, refresh the table and overlay on successful submission, show an error, close the overlay, or leave the view as it is                                                |

`GameSession` and the trade helpers do resource validation, so controller tests check how it forwards `give` and `receive`. 

**Step 4: Each choice**

| ID   | Methods under test                                                                  | State and action                                                                                                                                                               | Expected output                                                                                                                                                                                                                                                                                                                                                                      | Implemented? |
|------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| TC1  | `open()`, `requiredViewer()`, `render()`                                            | Trading is closed. Attempt to open and render for Red.                                                                                                                         | No refresh, `Color.NONE`, and an empty node with no trade actions.                                                                                                                                                                                                                                                                                                                   | :x:          |
| TC2  | `open()`, `requiredViewer()`, `render()`, `onOpenProposal()`, `onPropose()`         | Open with zero offers. Red opens the proposal form and offers Blue `[1 brick, 1 lumber]` for `[1 ore]`. The model registers it.                                                | The empty overview and the proposal form require Red. Opening the form refreshes the overlay. Submission sends one proposal with Red as proposer, Blue as receiver, and the two lists in the correct roles. Refreshes the table and shows the one pending offer.                                                                                                                     | :x:          |
| TC3  | `requiredViewer()`, `render()`, `onRequestResponse()`, `onAccept()`, `onFinalize()` | Two pending Red-to-Blue offers have identical terms and IDs `41` and `42`. Red requests a response to `42`. Render first for Red, then Blue. Blue accepts, then Red finalizes. | Both overview rows use their own IDs. The controller refreshes for the handoff, shows Red only the prompt for Blue, and shows Blue only offer `42` with response actions. Sends accept for Blue and `42` and refreshes the table and overview for Red. Only the accepted row has Finalize. Clicking it submits finalize for Red and `42` and refreshes the table and overview again. | :x:          |
| TC4  | `render()`, `onRequestResponse()`                                                   | Red clicks a response action captured on an overview. Before the click, the selected offer either disappeared while two others remained, or was accepted.                      | Each variant shows a trade error, keeps the overview for Red, and submits no trade command.                                                                                                                                                                                                                                                                                          | :x:          |
| TC5  | `render()`, `onDecline()`                                                           | Blue declines Red's only pending offer, `42`.                                                                                                                                  | Sends refusal for Blue and `42`. Refreshes the table and the empty overview for Red.                                                                                                                                                                                                                                                                                                 | :x:          |
| TC6  | `render()`, `onCounter()`, `onFinalize()`                                           | Blue counters `42`, giving `[1 ore]` for `[2 brick, 1 lumber]`. The model replaces it with pending counteroffer `57`. Red clicks Finalize.                                     | Sends `42` and a counteroffer from Blue to Red with the lists in the correct roles. Refreshes the table and overview, where `57` has a Finalize action. Sends finalize for Red and `57` directly, then refreshes the table and overview on success.                                                                                                                                  | :x:          |
| TC7  | `requiredViewer()`, `render()`, `onAccept()`                                        | Blue's response screen is rendered for `42`. The offer expires before Blue clicks Accept, and the model rejects the command. Trading stays open.                               | Shows a trade error. The next render clears the selection and shows the current overview for Red.                                                                                                                                                                                                                                                                                    | :x:          |
| TC8  | `requiredViewer()`, `render()`, `onOpenBankTrade()`, `onBankTrade()`                | Red opens the bank form and submits `4 brick` for `1 ore`. The model accepts.                                                                                                  | Refreshes the overlay to show the bank form for Red. Submits Red, brick as give, and ore as receive. Refreshes the table and overview.                                                                                                                                                                                                                                               | :x:          |
| TC9  | `render()`, `onBankTrade()`                                                         | Red submits `3 brick` for `1 ore`. The model rejects it.                                                                                                                       | Forwards the stacks as entered, shows a trade error, and keeps the bank form open without a success refresh.                                                                                                                                                                                                                                                                         | :x:          |
| TC10 | `requiredViewer()`, `render()`                                                      | Trading ends while the response screen is open.                                                                                                                                | Returns `Color.NONE` and renders an empty node with no trade actions.                                                                                                                                                                                                                                                                                                                | :x:          |
| TC11 | `onClose()`, `requiredViewer()`                                                     | Red closes the overview with no offer selected.                                                                                                                                | Requests overlay close and returns `Color.NONE`.                                                                                                                                                                                                                                                                                                                                     | :x:          |
| TC12 | `onClose()`, `open()`, `render()`                                                   | Blue closes a pending response. Try its old callback, then reopen on Red's turn.                                                                                               | Requests overlay close, clears the selection, and sends no command from the retired callback. Reopening shows Red's overview with the pending offer still listed.                                                                                                                                                                                                                    | :x:          |

