package br.com.power.correios.model;

public enum Status {
    NEED_SETUP, // Precisa baixar o CSV dos correios
    SETUP_RUNNING, // Esta baixando e salvando no banco
    READY;  // Serviço esta apto para ser consumido
}
