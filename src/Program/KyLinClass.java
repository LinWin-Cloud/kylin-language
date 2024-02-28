package Program;

import main.baseFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class KyLinClass {
    public String name;
    public boolean isPublic = false;
    public KyLinRuntime kyLinRuntime;
    public ArrayList<String> code = new ArrayList<>();

    public KyLinClass(String name)
    {
        this.name = name;
        kyLinRuntime = new KyLinRuntime(name);
    }

    public void init_class() throws Exception
    {
        for (int i = 0 ; i < this.code.size() ; i++)
        {
            String line = this.code.get(i);
            String[] words = line.split(" ");
            if (line.equals(""))
            {
                continue;
            }
            // 这个keyword对应一个hashmap中的value , 例如中文编程下 "变量 a = 1"，那么这个 "变量" 就会对应一个值 "var" ，同样可以处理语句
            if (line.startsWith("//"))
            {
                // 这条是处理注释，是注释的直接过去
                continue;
            }
            if (words[0].equals("public")) {
                kyLinRuntime.new_value(line , true);
                continue;
            }
            else if (words[0].equals("private")) {
                kyLinRuntime.new_value(line , false);
                continue;
            }
            else if (words[0].equals("func") || words[0].equals("f")) {
                /**
                 * func func_name (a ,b) public
                 *      return <a + b>
                 * end_func
                 *
                 * f func_name()
                 *      return "hello world"
                 * e_f
                 */
                String name = line.substring(line.indexOf(" ")+1,line.indexOf("(")).trim();
                String inputContent = line.substring(line.indexOf("(")+1 , line.lastIndexOf(")")).replace(" ","");
                boolean isPublic;

                if (words[0].equals("func"))
                {
                    isPublic = baseFunction.isPublic(line.substring(line.lastIndexOf(")")+1).trim());
                }else
                {
                    isPublic = false;
                }

                KyLinFunction kylinFunction = new KyLinFunction(name);
                kylinFunction.isPublic = isPublic;
                kylinFunction.setInput(inputContent.split(","));

                ArrayList<String> functionCode = new ArrayList<>();
                if (words[0].equals("func"))
                {
                    for (int j = i+1 ; j < this.code.size() ;j++) {
                        String line_1 = this.code.get(j).trim();
                        if ((line_1.equals("end_func"))) {
                            i = j;
                            break;
                        }
                        if (line_1.isEmpty()) {
                            continue;
                        }
                        functionCode.add(line_1);
                    }
                }else{
                    for (int j = i+1 ; j < this.code.size() ;j++)
                    {
                        String line_1 = this.code.get(j).trim();
                        if ((line_1.equals("e_f"))) {
                            i = j;
                            break;
                        }
                        if (line_1.isEmpty()) {
                            continue;
                        }
                        functionCode.add(line_1);
                    }
                }
                kylinFunction.kylinRuntime.code = functionCode;
                kylinFunction.kylinRuntime.PublicRuntime = kyLinRuntime;
                kyLinRuntime.FunctionMap.put(kylinFunction.name , kylinFunction);
            }
        }
    }
    public void run_init_() throws Exception {
        KyLinFunction kyLinFunction = this.kyLinRuntime.FunctionMap.get("__init__");
        if (kyLinFunction == null) {
            return;
        }else {
            kyLinFunction.kylinRuntime.run();
        }
    }
}
