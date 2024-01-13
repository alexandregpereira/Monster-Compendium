package br.alexandregpereira.hunter.state

interface StateHolderParams<Params> {

    var value: Params
}

fun <Params> StateHolderParams(initialParams: Params): StateHolderParams<Params> =
    StateHolderParamsImpl(initialParams)

private class StateHolderParamsImpl<Params>(
    initialParams: Params
) : StateHolderParams<Params> {

    override var value: Params = initialParams
}
