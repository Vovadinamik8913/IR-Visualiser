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
    const doneRes = await blob.text();
    return doneRes;
};