export const getScevInfo = async (irId, lineNumber) => {
    const scevInfoFormData = new FormData();
    scevInfoFormData.append("file", irId);
    scevInfoFormData.append("opt", 0);
    scevInfoFormData.append("line", lineNumber);
    const response = await fetch("/scev/get/scev/of/line", {
        method: 'POST',
        body: scevInfoFormData,
    });
    if(!response.ok) throw new Error("/get/scev/of/line bad request");
    const info = await response.text();
    return info;
}