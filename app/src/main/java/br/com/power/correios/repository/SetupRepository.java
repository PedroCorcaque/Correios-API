package br.com.power.correios.repository;

import org.apache.commons.lang3.StringUtils;
import br.com.power.correios.model.Address;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SetupRepository {

    @Value("${correios.base.url}")
    private String url;

    public List<Address> getFromOrigin() throws Exception {
        String resultStr = "";
        List<Address> resultList = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(this.url))) {
            HttpEntity entity = response.getEntity();
            resultStr = EntityUtils.toString(entity);
        }

        String[] resultStrSplited = resultStr.split("\n");

        for (String currentLine : resultStrSplited) {
            String[] currentLineSplited = currentLine.split(",");

            resultList.add(Address.builder()
                    .state(currentLineSplited[0])
                    .city(currentLineSplited[1])
                    .district(currentLineSplited[2])
                    .zipcode(StringUtils.leftPad(currentLineSplited[3], 8, "0"))
                    .street(currentLineSplited.length > 4 ? currentLineSplited[4] : null)
                    .build());
        }

        return resultList;
    }

}
