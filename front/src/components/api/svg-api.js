export const getFunctions = async (id) => {
    const getFunctionsFormData = new FormData();
    getFunctionsFormData.append("file", id);
    const response = await fetch('/files/get/functions', {
        method: 'POST',
        body: getFunctionsFormData,
    });
    if(!response.ok) throw new Error("/files/get/functions bad request");
    return await response.json();
}


export const getSvgByFunction = async (irId, funcName) => {
    const svgFormData = new FormData();
    svgFormData.append("file", irId);
    svgFormData.append("function", funcName);
    const response = await fetch('/files/get/svg', {
        method: 'POST',
        body: svgFormData,
    });
    if(!response.ok) throw new Error("/files/get/svg bad request");
    const reader = response.body.getReader();
    const contentLength = +response.headers.get('Content-Length');
    let receivedLength = 0;
    const chunks = [];

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      chunks.push(value);
      receivedLength += value.length;
    }

    const blob = new Blob(chunks);
    return await blob.text();
}


export const getSvgByLine = async (irId, index) => {
    const lineFormData = new FormData();
    lineFormData.append("file", irId);
    lineFormData.append("line", index - 1);
    const response = await fetch('/fromline/get/svg', {
        method: 'POST',
        body: lineFormData,
    });
    if(!response.ok) throw new Error("/fromline/get/svg bad request");
    const info = await response.json();
    return info;
}