import React, { useState, useEffect } from 'react';
import "../styles/OptTree.css";
import { getTree, loadProjects, deleteTreeNode } from '../api/opttree-api';

const OptTree = ({ isOpen, onClose, onSelect }) => {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  const [projectTree, setProjectTree] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!isOpen) return;
    try {
      loadProjects().then(data => setProjects(data));
    } catch (error) {
      console.error('Ошибка загрузки проектов:', error);
    }
  }, [isOpen]);

  const loadProjectTree = async (projectId) => {
    if(projectId === null) {
      setProjectTree([]);
    }
    setLoading(true);
    try {
      const treeData = await getTree(projectId);
      setProjectTree(treeData);
    } catch (error) {
      console.error('Ошибка загрузки дерева:', error);
    } finally {
      setLoading(false);
    }
  };

  const deleteNode = async (nodeId) => {
    try {
      await deleteTreeNode(nodeId);
      setProjectTree(prev => removeNodeFromTree(prev, nodeId));
    } catch (error) {
      console.error('Ошибка удаления:', error);
    }
  };

  const removeNodeFromTree = (nodes, targetId) => {
    if (!nodes) return [];
    return nodes
      .map(node => {
        if (node.id === targetId) return null;
        return {
          ...node,
          children: node.children 
            ? removeNodeFromTree(node.children, targetId)
            : []
        };
      })
      .filter(Boolean);
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
              if (e.target.value === '') {
                setSelectedProject(null);
                loadProjectTree(null);
                return;
              }
              const project = projects.find(p => p.id === parseInt(e.target.value, 10));
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

        {projectTree && projectTree.length > 0 && (
          <div className="tree-container">
            {projectTree.map(node => (
              <TreeNode
                key={node.id}
                node={node}
                onDelete={deleteNode}
                level={0}
                onSelect={onSelect}
              />
            ))}
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
       onSelect(node.id, node.flags);
    }
  }

  return (
    <div className="tree-node" style={{ marginLeft: `${level * 25}px` }}>
      <div className="node-header">
        <button className="node-button" onClick={handleSelect}>
          {node.children?.length > 0 && (
            <span>{isOpen ? '▼ ' : '▶ '}</span>
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