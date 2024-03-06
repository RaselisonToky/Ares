import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MySelectComponent from "../../../components/Select"
import styles from "../../../page.module.css";


function Add${entityName}() {

<#list fields as field>
    <#if field.isArmagedon == "true">
        const [${field.name}, set${field.name?cap_first}] = useState([]);
        const [selected${field.name?cap_first}, setSelected${field.name?cap_first}] = useState(null);
    <#else>
        const [${field.name}, set${field.name?cap_first}] = useState();
    </#if>
</#list>

<#list fields as field>
    <#if field.isArmagedon == "true">
    useEffect(() => {
        axios.get(`${'$'}{process.env.REACT_APP_API}/api/v1/${field.name}s`)
            .then(response => set${field.name?capitalize}(response.data))
            .catch(error => console.error('Error fetching ${field.name}:', error));
        }, []);

    const handle${field.name?cap_first}Change = (selected${field.name?cap_first}) => {
        setSelected${field.name?cap_first}(selected${field.name?cap_first});
    };
    </#if>
</#list>

    const handleSubmit = async () => {
        try {
            const response = await axios.post(`${'$'}{process.env.REACT_APP_API}/api/v1/${entityName?lower_case}s`, {
        <#list fields as field >
            <#if field.isArmagedon == "true">
                ${field.name} : selected${field.name?cap_first}.value,
            <#else>
                ${field.name},
            </#if>
        </#list>
            });
                console.log('${entityName} sauvegardé(e) avec succès:', response.data);
            } catch (error) {
                console.error('Erreur lors de la sauvegarde de ${entityName}:', error);
        }
    };

return (
    <div className={styles.super_container}>
        <div  className={styles.container}>
            <h2>Ajouter ${entityName}</h2>
            <form onSubmit={handleSubmit} className={styles.formgroup}>
                <#list fields as field>
                        <#if field.isArmagedon == "true">
                            <div className={styles.select_container}>
                                <label for="${field.name}" className={styles.select_label}>${field.name?capitalize}</label>
                                    <MySelectComponent
                                        label="${entityName?cap_first}"
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
                            <div className={styles.mb_4}>
                                <label for="${field.name}">${field.name?capitalize}</label>
                                            <input
                                        type="text"
                                        id="${field.name}"
                                        name="${field.name}"
                                        onChange={(e) => set${field.name?cap_first}(e.target.value)}
                                        className={styles.input}
                                />
                            </div>
                        </#if>

                </#list>
                <button type="submit" className={styles.button}>Ajouter</button>
            </form>
        </div>
    </div>
);
}

export default Add${entityName};