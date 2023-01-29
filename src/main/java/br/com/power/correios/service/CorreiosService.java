package br.com.power.correios.service;

import br.com.power.correios.CorreiosApplication;
import br.com.power.correios.exception.NoContentException;
import br.com.power.correios.exception.NotReadyException;
import br.com.power.correios.model.Address;
import br.com.power.correios.model.AddressStatus;
import br.com.power.correios.model.Status;
import br.com.power.correios.repository.AddressRepository;
import br.com.power.correios.repository.AddressStatusRepository;
import br.com.power.correios.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressStatusRepository addressStatusRepository;

    @Autowired
    private SetupRepository setupRepository;

    @Value("${setup.on.startup}")
    private boolean setupOnStartup;

    public Status getStatus() {
        return this.addressStatusRepository.findById(AddressStatus.DEFAULT_ID)
                .orElse(AddressStatus.builder().status(Status.NEED_SETUP).build())
                .getStatus();
    }

    public Address getAddressByZipcode(String zipcode) throws NoContentException, NotReadyException {

        if (!this.getStatus().equals(Status.READY))
            throw new NotReadyException();

        return addressRepository.findById(zipcode)
                .orElseThrow(NoContentException::new);
    }

    private void saveStatus(Status status) {
        this.addressStatusRepository.save(AddressStatus.builder()
                .id(AddressStatus.DEFAULT_ID)
                .status(status)
                .build());
    }

    @EventListener(ApplicationStartedEvent.class)
    protected void setupOnStartup() {
        if (!setupOnStartup)
            return;

        try {
            this.setup();
        } catch(Exception exc) {
            CorreiosApplication.close(999);
            logger.error(".setupOnStartup() - Exception", exc);
        }
    }

    public void setup() throws Exception {
        if (this.getStatus().equals(Status.NEED_SETUP)) {
            this.saveStatus(Status.SETUP_RUNNING);

            try {
                this.addressRepository.saveAll(
                    this.setupRepository.getFromOrigin());
            } catch(Exception exc) {
                this.saveStatus(Status.NEED_SETUP);
                throw exc;
            }

            this.saveStatus(Status.READY);
        }
    }
}
