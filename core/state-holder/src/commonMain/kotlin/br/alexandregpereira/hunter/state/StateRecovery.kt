package br.alexandregpereira.hunter.state

interface StateRecovery<State> {

    fun getState(): State

    fun saveState(state: State)
}
