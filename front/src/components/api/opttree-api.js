export const loadProjects = async () => {
    const response = await fetch('/files/get/projects', {
        method: 'POST',
    });
    if(!response.ok) throw new Error("/files/get/projects bad request");
    return await response.json();
};

export const getTree = async (project) => {
    const formData = new FormData();
    formData.append("project", project);
    const response = await fetch(`/tree/get`, {
      method: 'POST',
      body: formData,
    });
    if(!response.ok) throw new Error("/tree/get bad request");
    return await response.json();
};


export const deleteTreeNode = async (node) => {
    const formData = new FormData();
    formData.append("node", node);
    const response = await fetch(`/tree/delete`, {
      method: 'DELETE',
      body: formData,
    });
    if(!response.ok) throw new Error("/tree/delete bad request");
};