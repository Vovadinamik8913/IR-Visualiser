import config from '../config/config.js';

export const getScevInfo = async (irId, lineNumber) => {
    const scevInfoFormData = new FormData();
    scevInfoFormData.append("file", irId);
    scevInfoFormData.append("line", lineNumber);
    const response = await fetch(`${config.api.baseUrl}/scev/get/scev/of/line`, {
        method: 'POST',
        body: scevInfoFormData,
    });
    if(!response.ok) throw new Error("/get/scev/of/line bad request");
    return await response.text();
}

export const getScevLoopInfo = async (irId, funcName, svgBlock) => {
    const loopInfoFormData = new FormData();
    loopInfoFormData.append("file", irId);
    loopInfoFormData.append("function", funcName);
    loopInfoFormData.append("block", "%" + svgBlock);
    const response = await fetch(`${config.api.baseUrl}/scev/get/scev/loop/count`, {
        method: 'POST',
        body: loopInfoFormData,
    });
    if(!response.ok) throw new Error("/scev/get/scev/loop/count");
    return await response.text();
}