export const getSvgWithLoops = async (irId, funcName) => {
    const svgLoopsFormData = new FormData();
    svgLoopsFormData.append("file", irId);
    svgLoopsFormData.append("opt", 0);
    svgLoopsFormData.append("function", funcName);
    const response = await fetch('/loops/get/all/svg', {
        method: 'POST',
        body: svgLoopsFormData,
    });
    if(!response.ok) throw new Error("/loops/get/svg bad request");
    return await response.text();
}

export const getLoopInfo = async (irId, funcName, svgBlock) => {
    const loopInfoFormData = new FormData();
    loopInfoFormData.append("file", irId);
    loopInfoFormData.append("opt", 0);
    loopInfoFormData.append("function", funcName);
    loopInfoFormData.append("block", svgBlock);
    const response = await fetch('/loops/get/block/by/name', {
        method: 'POST',
        body: loopInfoFormData,
    });
    if(!response.ok) throw new Error("/loops/get/block/by/name bad request");
    return await response.text();
}

export const getNestedLoops= async (irId, funcName, svgBlock, howManyClicks) => {
    const loopInfoFormData = new FormData();
    loopInfoFormData.append("file", irId);
    loopInfoFormData.append("opt", 0);
    loopInfoFormData.append("function", funcName);
    loopInfoFormData.append("block", svgBlock);
    loopInfoFormData.append("click", howManyClicks);
    const response = await fetch('/loops/get/loop/svg/by/click', {
        method: 'POST',
        body: loopInfoFormData,
    });
    if(!response.ok) throw new Error("/loops/get/loop/svg/by/click bad request");
    return await response.text();
}