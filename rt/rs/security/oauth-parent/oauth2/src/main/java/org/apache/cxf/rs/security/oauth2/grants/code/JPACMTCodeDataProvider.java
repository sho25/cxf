begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|grants
operator|.
name|code
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityTransaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PersistenceContext
import|;
end_import

begin_class
specifier|public
class|class
name|JPACMTCodeDataProvider
extends|extends
name|JPACodeDataProvider
block|{
annotation|@
name|PersistenceContext
specifier|private
name|EntityManager
name|entityManager
decl_stmt|;
comment|/**      * Returns the entityManaged used for the current operation.      */
annotation|@
name|Override
specifier|protected
name|EntityManager
name|getEntityManager
parameter_list|()
block|{
return|return
name|this
operator|.
name|entityManager
return|;
block|}
specifier|public
name|void
name|setEntityManager
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|)
block|{
name|this
operator|.
name|entityManager
operator|=
name|entityManager
expr_stmt|;
block|}
comment|/**      * Doesn't do anything, beginning tx is handled by container.      */
annotation|@
name|Override
specifier|protected
name|EntityTransaction
name|beginIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Doesn't do anything, commit is handled by container.      */
annotation|@
name|Override
specifier|protected
name|void
name|commitIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{     }
comment|/**      * Doesn't do anything, em lifecycle is handled by container.      */
annotation|@
name|Override
specifier|protected
name|void
name|closeIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{     }
block|}
end_class

end_unit

