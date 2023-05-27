package program.vm;

import com.kylin.Runtime.Expression;
import com.kylin.Runtime.MainRuntime;

public class Fix {
    public static void FixValue(
            String code,
            MainRuntime mainRuntime,
            int line)
    throws Exception
    {
        String[] words = code.split(" ");
        String name = words[1];
        String value = code.substring(code.indexOf("=")+1).trim();
        if (
                mainRuntime.PublicRuntime != null
                        &&
                        mainRuntime.PublicRuntime.ValueMap.containsKey(name)) {

            if (mainRuntime.PublicRuntime.ValueMap.get(name).getType().equals("obj")) {
                throw new Exception("Can not fix a 'obj' type value.");
            }
            mainRuntime.PublicRuntime.ValueMap.get(name).setContent(Expression.getExString(value, line, mainRuntime));
        }


        else if (!mainRuntime.ValueMap.containsKey(name)) {
            throw new Exception("Can not find target value: "+name);
        }


        else if (mainRuntime.ListMap.containsKey(name))
        {
            throw new Exception("Can not fix a 'list' type value.");
        }


        else {
            if (
                    mainRuntime.ValueMap.get(name).getType().equals("obj")
            )
            {
                throw new Exception("Can not fix a 'obj' type value.");
            }
            mainRuntime.ValueMap.get(name).setContent(Expression.getExString(value,line,mainRuntime));
        }
    }
}
