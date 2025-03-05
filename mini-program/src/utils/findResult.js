// 导出一个名为 findPersonalityType 的函数，用于根据用户答案、问题和结果集合找出用户的个性类型
export function findPersonalityType(userAnswers, questions, results) {
    // 初始化结果计数器对象，用于统计每个结果的出现次数
    const resultCounter = {};

    // 初始化结果计数器
    results.forEach(result => {
        resultCounter[result.resultName] = 0;
    });

    // 遍历用户答案，统计每个结果的出现次数
    userAnswers.forEach((answerKey, i) => {
        const resultChar = questions[i].options.find(option => option.key === answerKey).result;
        results.forEach(result => {
            if (result.resultProp.includes(resultChar)) {
                resultCounter[result.resultName]++;
            }
        });
    });

    // 查找分数最高的结果
    let maxScore = -1;
    let maxResult = null;
    for (const [resultName, score] of Object.entries(resultCounter)) {
        if (score > maxScore) {
            maxScore = score;
            maxResult = results.find(result => result.resultName === resultName);
        }
    }

    return maxResult;
}