package al.jdi.cti;


public interface PredictiveListener {
	void chamadaAtendida(int callId);

	void chamadaEncerrada(int callId, int causa);

	void chamadaErro(Exception e);

	void chamadaIniciada(int callId);

	void chamadaInvalida(int callId, int causa);

	void chamadaNoAgente(int callId, String agente);
	
	void chamadaEmFila(int callId);
}
