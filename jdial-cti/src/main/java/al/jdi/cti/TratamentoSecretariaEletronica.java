package al.jdi.cti;

import javax.telephony.callcenter.CallCenterCall;

public enum TratamentoSecretariaEletronica {
	DESLIGAR(CallCenterCall.ANSWERING_TREATMENT_DROP), TRANSFERIR(
			CallCenterCall.ANSWERING_TREATMENT_NONE);

	private final int valor;

	public int getValor() {
		return valor;
	}

	TratamentoSecretariaEletronica(int valor) {
		this.valor = valor;
	}

}
