declare var require: any

export const MAP_MAPPING = {
    'INDIA': {
        key: 'countries/in/in-all'
    },
    'WORLD': {
        key: 'custom/world'
    },
    'US': {
        key: 'custom/usa-and-canada'
    }
}

require('../../assets/maps/in-all.js')
require('../../assets/maps/world.js')
require('../../assets/maps/usa-and-canada.js')