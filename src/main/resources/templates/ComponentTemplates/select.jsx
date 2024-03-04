import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

const MySelectComponent = ({options, width, onChange, selectedValue ,label}) => {

    const handleChange = (event) => {
        const selectedOption = options.find(option => option.value === event.target.value);
        onChange(selectedOption);
    };

    return (
        <FormControl style={{ width: width }}>
            <InputLabel id="bouquet-select-label">{label}</InputLabel>
            <Select
                labelId="bouquet-select-label"
                id="bouquet-select"
                value={selectedValue ? selectedValue.value : ''}
                label="Sélectionnez un bouquet"
                onChange={handleChange}
            >
                {options.map((option) => (
                    <MenuItem key={option.test} value={option.value}>
                        {option.label}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
};

export default MySelectComponent;