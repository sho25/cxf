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
name|sts
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_comment
comment|/**  * This interface defines a pluggable way of mapping an identity from a source realm to a target  * realm.  */
end_comment

begin_interface
specifier|public
interface|interface
name|IdentityMapper
block|{
comment|/**      * Map a principal in the source realm to the target realm      * @param sourceRealm the source realm of the Principal      * @param sourcePrincipal the principal in the source realm      * @param targetRealm the target realm of the Principal      * @return the principal in the target realm      */
name|Principal
name|mapPrincipal
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|Principal
name|sourcePrincipal
parameter_list|,
name|String
name|targetRealm
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

