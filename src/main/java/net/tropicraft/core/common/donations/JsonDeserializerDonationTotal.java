package net.tropicraft.core.common.donations;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;

public class JsonDeserializerDonationTotal implements JsonDeserializer<JsonDataDonation>  {

    @Override
    public JsonDataDonation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        JsonDataDonation jsonReturn = new JsonDataDonation();

        JsonElement jsonMeta = obj.get("meta");
        JsonDataDonationMeta meta = (new Gson()).fromJson(jsonMeta, JsonDataDonationMeta.class);

        jsonReturn.meta = meta;

        JsonObject jsonData = obj.get("data").getAsJsonObject();
        int totalDonated = jsonData.get("amountRaised").getAsInt();

        jsonReturn.totalDonated = totalDonated;

        return jsonReturn;

    }

}
