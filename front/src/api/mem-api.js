import config from "../config/config";

export const getMemInfo = async (irId, lineNumber) => {
    const memInfoFormData = new FormData();
    memInfoFormData.append("file", irId);
    memInfoFormData.append("line", lineNumber);
    const response = await fetch(`${config.api.baseUrl}/memoryssa/get/memoryssa/of/line`, {
        method: 'POST',
        body: memInfoFormData,
    });
    if(!response.ok) throw new Error("/memoryssa/get/memoryssa/of/line bad request");
    return await response.text();
}

export const getMemOfAccess = async (irId, functionName, access) => {
    const memOfAccessFormData = new FormData();
    memOfAccessFormData.append("file", irId);
    memOfAccessFormData.append("functionName", functionName);
    memOfAccessFormData.append("access", access);
    const response = await fetch( `${config.api.baseUrl}/memoryssa/get/memoryssa/of/access`, {
        method: 'POST',
        body: memOfAccessFormData,
    });
    if(!response.ok) throw new Error("/get/memoryssa/of/access bad request");
    return parseInt(await response.text());
}