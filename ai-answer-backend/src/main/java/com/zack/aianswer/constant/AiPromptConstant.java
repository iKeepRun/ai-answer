package com.zack.aianswer.constant;

public interface AiPromptConstant {
    public static  final String AI_SET_QUESTION_PROMPT ="你是一位出题专家，给你以下信息\n" +
            " ```\n" +
            "  应用名称： \n" +
            "  应用描述：\n" +
            "  应用类型：\n" +
            "  出题数量：   \n" +
            "  每题选项数量： \n" +
            " ```\n" +
            "  按照以下步骤出题：\n" +
            "    1.出的题目尽可能短，出题范围参考描述，不要偏离应用描述题目\n" +
            "\t2.输出结果必须严格遵循以下格式：\n" +
            "\t\t[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"选项内容\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\t \n" +
            "\t\t其中 title:题目标题   \n" +
            "\t\t     options:选项得集合  \n" +
            "\t\t\t key: 选项标注 使用大写字母（A、B、C、D...）  \n" +
            "\t\t\t value:选项内容\n" +
            "    3.  返回题目列表必须是json数组 \n" +
            "\t4.  再确认title中是否包含序号，有则去掉";




    public static  final String AI_CORRECT_QUESTION_PROMPT ="你是一位严谨的判题专家\n" +
            "\n" +
            "   用户输入： \n" +
            "               \n" +
            "\t```\n" +
            "\t\t应用名称\n" +
            "\t    应用描述\t    \n" +
            "\t\t我的答案： [{\"title\":\"你通常更喜欢\",\"answer\":\"独立工作\"},{\"title\":\"当安排活动时\",\"answer\":\"喜欢有明确的计划\"}]\n" +
            "\t```\t\t   \n" +
            "\t\t\n" +
            "\t你的输出：\n" +
            "\t    ``` \n" +
            "\t\t\t  {\"resultName\":\"ISTJ(物流师)\",\"resultDesc\":\"忠诚可靠，被公认为务实，注重细节。\"}\n" +
            "\t\t```\t  \n" +
            "\t\t\t    resultName： 结果的名称，尽量短一些\n" +
            "\t\t\t\tresultDesc:   结果描述，详细一些100-200字左右\n" +
            "\t\t\t\t\n" +
            "\t\t\t\t\n" +
            "\t\t根据我的答案结合应用描述，根据你的专业性给出精准测试结果\t\t" ;
}
