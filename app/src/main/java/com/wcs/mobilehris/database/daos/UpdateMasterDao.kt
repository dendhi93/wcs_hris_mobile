package com.wcs.mobilehris.database.daos

import androidx.room.*
import com.wcs.mobilehris.database.entity.UpdateMasterEntity

@Dao
interface UpdateMasterDao{

    @Query("SELECT * FROM mUpdate_master_data")
    fun getDataUpdate() : List<UpdateMasterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpdateMaster(updateMasterEntity: UpdateMasterEntity)

    @Delete
    fun deleteUpdateMaster(updateMasterEntity: UpdateMasterEntity)

}