package android.ohdm.de.editor.api;

import android.util.Log;

public class ApiException extends Exception
{
    public ApiException()
    {
        Log.d("Expection","ApiException wurde geworfen. ");
    }

    public ApiException(String message)
    {
        super(message);
    }
}
