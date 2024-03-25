import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MySelectComponent from "../../../components/Select"
import { useParams } from 'react-router-dom';
import styles from "../../../page.module.css";

const Update${entityName} = () => {
    const itemId = useParams().id;
<#list fields as field>
    <#if field.isArmagedon == "true">
    const [${field.name}, set${field.name?cap_first}] = useState([]);
    const [selected${field.name?cap_first}, setSelected${field.name?cap_first}] = useState(null);
    <#else>
    const [${field.name}, set${field.name?cap_first}] = useState();
    </#if>
</#list>

    useEffect(() => {
        const token = localStorage.getItem('token');
            const axiosConfig = {
                headers: {
                'Authorization': 'Bearer ' + token
            }
        };
        axios.get(`${'$'}{process.env.REACT_APP_API}/api/v1/${entityName?lower_case}s/${'$'}{itemId}`, axiosConfig)
        .then(response => {
<#list fields as field>
    <#if field.isArmagedon == "true">
            setSelected${field.name?cap_first}(response.data.${field.name});
    <#else>
            set${field.name?cap_first}(response.data.${field.name})
    </#if>
</#list>
        })
        .catch(error => console.error('Error fetching ${entityName}:', error));
    }, [itemId]);


<#list fields as field>
    <#if field.isArmagedon == "true">
    useEffect(() => {
        const token = localStorage.getItem('token');
        const axiosConfig = {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        };
        axios.get(`${'$'}{process.env.REACT_APP_API}/api/v1/${field.name}s`,axiosConfig)
        .then(response => set${field.name?capitalize}(response.data))
        .catch(error => console.error('Error fetching ${field.name}:', error));
    }, []);

    const handle${field.name?cap_first}Change = (selected${field.name?cap_first}) => {
    setSelected${field.name?cap_first}(selected${field.name?cap_first});
    };
    </#if>
</#list>



    const handleSubmit = async () => {
        const token = localStorage.getItem('token');
        const axiosConfig = {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        };
        try {
            const new${entityName} = {
            <#list fields as field >
                <#if field.isArmagedon == "true">
                ${field.name} : selected${field.name?cap_first}.value,
                <#else>
                ${field.name},
                </#if>
            </#list>
            };
            await axios.put(`${'$'}{process.env.REACT_APP_API}/api/v1/${entityName?lower_case}s/update/${'$'}{itemId}`, new${entityName}, axiosConfig);
        } catch (error) {
            console.error('Error updating item:', error);
        }
    };

    return (
        <div className={styles.super_container}>
            <div className={styles.container}>
                <h2 className={styles.h2title} >Modifier l'élément</h2>
                <form onSubmit={handleSubmit} className={styles.formgroup}>
                    <#list fields as field>
                            <#if field.isArmagedon == "true">
                                <div className={styles.select_container}>
                                    <label for="${field.name}"  className={styles.select_label}>${field.name?capitalize}</label>
                                    <MySelectComponent
                                        label="${field.name?cap_first}"
                                        width="300px"
                                        options={${field.name}.map(item => ({
                                        value: item,
                                        label: item.${field.name},
                                        test: item.id_${field.name}
                                        }))}
                                        onChange={handle${field.name?cap_first}Change}
                                        selectedValue={selected${field.name?cap_first}}
                                        className={styles.select_component}
                                    />
                                </div>

                            <#elseif field.name?contains("_id")>

                            <#else >
                                <div className={styles.champ}>
                                    <label for="${field.name}" className={styles.label}>${field.name?capitalize}</label>
                                    <input
                                        type="text"
                                        id="${field.name}"
                                        name="${field.name}"
                                        value={${field.name}}
                                        onChange={(e) => set${field.name?cap_first}(e.target.value)}
                                        className={styles.input}
                                    />
                                </div>

                            </#if>
                    </#list>
                    <button type="submit">Enregistrer</button>
                </form>
            </div>
        </div>
    );
};

export default Update${entityName};
