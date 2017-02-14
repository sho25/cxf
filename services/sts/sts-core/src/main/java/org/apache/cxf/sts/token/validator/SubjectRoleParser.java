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
operator|.
name|token
operator|.
name|validator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_comment
comment|/**  * This interface defines a way to extract roles from a JAAS Subject  */
end_comment

begin_interface
specifier|public
interface|interface
name|SubjectRoleParser
block|{
comment|/**      * Return the set of User/Principal roles from the Subject.      * @param principal the optional Principal      * @param subject the JAAS Subject      * @return the set of User/Principal roles from the Subject.      */
name|Set
argument_list|<
name|Principal
argument_list|>
name|parseRolesFromSubject
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|Subject
name|subject
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

