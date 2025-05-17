import React, { useState, useEffect } from 'react';
import "./styles/OptTree.css";


const DomTree = ({ isOpen, onClose, onSelect, onLoad }) => {
  const [domTree, setDomTree] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!isOpen) return;
    try {
      loadDomTree();
    } catch (error) {
      console.error('Ошибка загрузки проектов:', error);
    }
  }, [isOpen]);

  const loadDomTree = async () => {
    setLoading(true);
    try {
      const treeData = await onLoad();
      setDomTree(treeData);
    } catch (error) {
      console.error('Ошибка загрузки дерева:', error);
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2 className="title">Дерево доминации</h2>
        <button onClick={onClose} className="close-btn">X</button>

        {loading && <div className="loading">Загрузка дерева...</div>}

        {domTree && (
          <div className="tree-container">
            {
              <TreeNode
                node={domTree}
                level={0}
                onSelect={onSelect}
              />
            }
          </div>
        )}
      </div>
    </div>
  );
};

const TreeNode = ({ node, level, onSelect }) => {
  const [isOpen, setIsOpen] = useState(false);

  const handleSelect = () => {
    setIsOpen(!isOpen);
    if(isOpen === true && node?.block?.label) {
       onSelect(node.block.label);
    }
  }

  return (
    <div className="tree-node" style={{ marginLeft: `${level * 25}px` }}>
      <div className="node-header">
        <button className="node-button" onClick={handleSelect}>
          {node.children?.length > 0 && (
            <span>{isOpen ? '▼ ' : '▶ '}</span>
          )}
          {node.block?.label}
        </button>
      </div>

      {isOpen && node.children?.map(child => (
        <TreeNode
          node={child}
          level={level + 1}
          onSelect={onSelect}
        />
      ))}
    </div>
  );
};

export default DomTree;