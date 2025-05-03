package com.magic.system;

import lombok.Getter;

// This class is a placeholder for the Result class in the com.magic.system package.
// It is used to encapsulate the response data for API calls.
@Getter
public class Result {
   private boolean flag; // true ; success
   private Integer code; // 400 ; error, 200; success
   private String message; // error message(e.g., "Find all success")
   private Object data; // The response payload

   public Result(){};
   public Result(boolean flag, Integer code, String message, Object data){
      this.flag = flag;
      this.code = code;
      this.message = message;
      this.data = data;
   }

   public Result(boolean flag, Integer code, String message){
      this.flag = flag;
      this.code = code;
      this.message = message;
   }
}
