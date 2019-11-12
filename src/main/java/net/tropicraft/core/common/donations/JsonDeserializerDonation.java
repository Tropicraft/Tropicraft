package net.tropicraft.core.common.donations;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;

public class JsonDeserializerDonation implements JsonDeserializer<JsonDataDonation>  {

    @Override
    public JsonDataDonation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        boolean oldTest = false;
        if (oldTest) {
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
                entry.id = objTemplate.get("id").getAsInt();

                jsonReturn.new_donations.add(entry);
            }

            return null;
        } else {
            JsonObject obj = json.getAsJsonObject();

            JsonDataDonation jsonReturn = new JsonDataDonation();

            JsonElement jsonMeta = obj.get("meta");
            JsonDataDonationMeta meta = (new Gson()).fromJson(jsonMeta, JsonDataDonationMeta.class);

            jsonReturn.meta = meta;

            Iterator<JsonElement> arrIt = obj.get("data").getAsJsonArray().iterator();
            while (arrIt.hasNext()) {
                JsonObject objTemplate = arrIt.next().getAsJsonObject();
                JsonDataDonationEntry entry = (new Gson()).fromJson(objTemplate, JsonDataDonationEntry.class);
                jsonReturn.new_donations.add(entry);
            }

            return jsonReturn;

        }

    }

}
