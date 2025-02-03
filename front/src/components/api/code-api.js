export const getCode = async (id) => {
    const buildFormData = new FormData();
    buildFormData.append("file", id);
    const response = await fetch('/files/get/code', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/build/file bad request");
    const blob = await response.blob();
    const doneRes = await blob.text();
    return doneRes;
};