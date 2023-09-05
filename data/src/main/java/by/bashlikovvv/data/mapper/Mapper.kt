package by.bashlikovvv.data.mapper

interface Mapper<Entity, Domain> {

    fun mapFromEntity(entity: Entity): Domain

    fun mapToEntity(domain: Domain): Entity
}