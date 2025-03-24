export const getCode = async (id) => {
    const buildFormData = new FormData();
    buildFormData.append("file", id);
    const response = await fetch('/files/get/code', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/build/file bad request");
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
};

export const getLineNumberFromBlock = async (id, blockTitle, funcName) => {
    const lineNumberFormData = new FormData();
    lineNumberFormData.append("file", id);
    lineNumberFormData.append("id", blockTitle);
    lineNumberFormData.append("function", funcName);
    const response = await fetch('/fromline/get/line', {
        method: 'POST',
        body: lineNumberFormData,
    });

    if(!response.ok) throw new Error("/fromline/get/line bad request");
    return parseInt(await response.text());
}