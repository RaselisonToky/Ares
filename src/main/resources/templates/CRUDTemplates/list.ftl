import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Table from "../../../components/Table";
import { Link } from 'react-router-dom';

const List${entityName} = () => {
    const [${entityName?lower_case}, set${entityName}] = useState([]);
    const [selected${entityName}, setSelected${entityName}] = useState(null);

    const handleRowClick = (selectedData) => {
        setSelected${entityName}(selectedData);
    };



    useEffect(() => {
        const fetchItems = async () => {
        try {
            const response = await axios.get(`${'$'}{process.env.REACT_APP_API}/api/v1/${entityName?lower_case}s`);
            set${entityName}(response.data);
        } catch (error) {
            console.error('Error fetching items:', error);
        }
        };

        fetchItems();
    }, []);

    const columns = [
    <#list fields as field>
      '${field.name?cap_first}',
    </#list>
    ];

    const data = ${entityName?lower_case}.map(item => ({
    <#list fields as field>
        <#if field.isArmagedon == 'true'>
        ${field.name}: item.${field.name}.${field.name},
        <#else>
        ${field.name}: item.${field.name},
        </#if>

    </#list>
    }));




return (
    <div>
        <h1>Liste des éléments</h1>
        <div>
            <Table columns={columns} data={data}  onRowClick={handleRowClick} />
        </div>
        <div>
            {selected${entityName} && (
            <div>
                <div>Options pour {selected${entityName}.nom} :</div>
                <div>
                    <button>
                        <Link to={`/edit_${entityName?lower_case}/${'$'}{selected${entityName}.${entityName?lower_case}_id}`}>Modifier</Link>
                    </button>
                    <button>Supprimer</button>
                </div>
            </div>
            )}
        </div>
    </div>
);
};

export default List${entityName};
