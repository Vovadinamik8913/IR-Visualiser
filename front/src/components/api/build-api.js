export const buildByFile = async (folder, file) => {
    const buildFormData = new FormData();
    buildFormData.append("folder", folder);
    buildFormData.append("opt", 0);
    buildFormData.append("file", file);
    const response = await fetch('/files/build/file', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/build/file bad request");
    const doneRes = await response.text();
    return doneRes;
};

export const buildByPath = async (folder, path) => {
    const buildFormData = new FormData();
    buildFormData.append("folder", folder);
    buildFormData.append("opt", 0);
    buildFormData.append("path", path);
    const response = await fetch('/files/build/path', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/build/path bad request");
    const doneRes = await response.text();
    return doneRes;
};