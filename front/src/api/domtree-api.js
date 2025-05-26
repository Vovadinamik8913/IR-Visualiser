import config from '../config/config.js';

export const getDomTreeChildren = async (irId, funcName, block) => {
    const formData = new FormData();
    formData.append("file", irId);
    formData.append("function", funcName);
    formData.append("block", block)
    const response = await fetch(`${config.api.baseUrl}/domtree/get/children`, {
        method: 'POST',
        body: formData,
    });
    if(!response.ok) throw new Error("/domtree/get/children bad request");
    const children = await response.text();
    return children;
}

export const getDomTree = async (irId, funcName) => {
    const formData = new FormData();
    formData.append("file", irId);
    formData.append("function", funcName);
    const response = await fetch(`${config.api.baseUrl}/domtree/get`, {
        method: 'POST',
        body: formData,
    });
    if(!response.ok) throw new Error("/domtree/get bad request");
    const domtree = await response.json();
    console.log(domtree);
    return domtree;
}