export const saveByFile = async (folder, file) => {
    const buildFormData = new FormData();
    buildFormData.append("folder", folder);
    buildFormData.append("file", file);
    const response = await fetch('/files/save/file', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/save/file bad request");
    const doneRes = await response.text();
    return doneRes;
};

export const saveByPath = async (folder, path) => {
    const buildFormData = new FormData();
    buildFormData.append("folder", folder);
    buildFormData.append("filePath", path);
    const response = await fetch('/files/save/path', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/build/path bad request");
    const doneRes = await response.text();
    return doneRes;
};

export const generateSvg = async(id) => {
    const buildFormData = new FormData();
    buildFormData.append("file", id);
    buildFormData.append("opt", 0);
    const response = await fetch('/files/generate', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/files/generate/bad request");
}

export const optimize = async(id, flags) => {
    const buildFormData = new FormData();
    buildFormData.append("file", id);
    buildFormData.append("opt", 0);
    buildFormData.append("flags", flags);
    const response = await fetch('/tree/add', {
        method: 'POST',
        body: buildFormData,
    });
    if(!response.ok) throw new Error("/tree/add bad request");
    const doneRes = await response.text();
    return doneRes;
}