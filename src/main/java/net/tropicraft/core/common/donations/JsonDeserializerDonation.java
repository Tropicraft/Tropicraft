package net.tropicraft.core.common.donations;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;

public class JsonDeserializerDonation implements JsonDeserializer<JsonDataDonationOld>  {

    @Override
    public JsonDataDonationOld deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement eleArr = obj.get("new_donations");

        JsonDataDonationOld jsonReturn = new JsonDataDonationOld();

        JsonArray arr = eleArr.getAsJsonArray();
        Iterator<JsonElement> arrIt = arr.iterator();
        while (arrIt.hasNext()) {
            JsonElement eleTemplate = arrIt.next();
            JsonObject objTemplate = eleTemplate.getAsJsonObject();

            String name = objTemplate.get("name").getAsString();
            String amount = objTemplate.get("amount").getAsString();

            JsonDataDonationEntryOld entry = new JsonDataDonationEntryOld();
            entry.name = name;
            entry.amount = amount;

            jsonReturn.new_donations.add(entry);
        }

        return jsonReturn;

    }

}
