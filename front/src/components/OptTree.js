import React, { useState, useEffect } from 'react';
import "./styles/OptTree.css";

const OptTree = ({ isOpen, onClose, onSelect }) => {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  const [projectTree, setProjectTree] = useState(null);
  const [loading, setLoading] = useState(false);

  // Загрузка проектов
  useEffect(() => {
    if (!isOpen) return;
    
    fetch('/files/get/projects', {
      method: 'POST'
    })
      .then(res => res.json())
      .then(data => setProjects(data));
  }, [isOpen]);

  // Загрузка дерева проекта
  const loadProjectTree = async (projectId) => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("project", projectId);
      const response = await fetch(`/tree/get`, {
        method: 'POST',
        body: formData,
      });
      const treeData = await response.json();
      setProjectTree(treeData);
    } catch (error) {
      console.error('Ошибка загрузки дерева:', error);
    } finally {
      setLoading(false);
    }
  };

  // Удаление узла
  const deleteNode = async (nodeId) => {
    try {
      await fetch(`/api/nodes/${nodeId}`, { method: 'DELETE' });
      setProjectTree(prev => removeNodeFromTree(prev, nodeId));
    } catch (error) {
      console.error('Ошибка удаления:', error);
    }
  };

  // Рекурсивное удаление узлов
  const removeNodeFromTree = (node, targetId) => {
    if (!node) return null;
    if (node.id === targetId) return null;
    return {
      ...node,
      children: node.children 
        ? node.children.map(child => removeNodeFromTree(child, targetId)).filter(Boolean)
        : []
    };
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2 className="title">Оптимизационное дерево</h2>
        <button onClick={onClose} className="close-btn">X</button>
        
        <div className="project-selector">
          <select
            value={selectedProject?.id || ''}
            onChange={(e) => {
              const project = projects.find(p => p.id == e.target.value);
              setSelectedProject(project);
              loadProjectTree(project.id);
            }}
            className="dropdown"
          >
            <option value="">Выберите проект</option>
            {projects.map(project => (
              <option key={project.id} value={project.id}>
                {project.name}
              </option>
            ))}
          </select>
        </div>

        {loading && <div className="loading">Загрузка дерева...</div>}

        {projectTree && (
          <div className="tree-container">
            <TreeNode
              node={projectTree}
              onDelete={deleteNode}
              level={0}
              onSelect={onSelect}
            />
          </div>
        )}
      </div>
    </div>
  );
};

const TreeNode = ({ node, onDelete, level, onSelect }) => {
  const [isOpen, setIsOpen] = useState(false);

  const handleSelect = () => {
    setIsOpen(!isOpen);
    if(isOpen === true) {
       onSelect(node.id);
    }
  }

  return (
    <div className="tree-node" style={{ marginLeft: `${level * 25}px` }}>
      <div className="node-header">
        <button onClick={handleSelect}>
          {node.children?.length > 0 && (
            <span>{isOpen ? '▼' : '▶'}</span>
          )}
          {node.name}
        </button>
        
        <button 
          onClick={() => onDelete(node.id)}
          className="delete-btn"
        >
          Удалить
        </button>
      </div>

      {isOpen && node.children?.map(child => (
        <TreeNode
          key={child.id}
          node={child}
          onDelete={onDelete}
          level={level + 1}
          onSelect={onSelect}
        />
      ))}
    </div>
  );
};

export default OptTree;