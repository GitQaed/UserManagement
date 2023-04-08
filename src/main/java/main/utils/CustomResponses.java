package main.utils;


import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class CustomResponses {
    public static <T> ResponseEntity<?> successResponse(int status, T data) {

        JSONObject response = new JSONObject();
        response.put("data", data);
        response.put("error", false);

        return ResponseEntity.status(status).body(response.toMap());
    }

    public static <T> ResponseEntity<?> errorResponse(int status, T data) {

        JSONObject response = new JSONObject();
        response.put("message", data);
        response.put("error", true);

        return ResponseEntity.status(status).body(response.toMap());
    }
}
