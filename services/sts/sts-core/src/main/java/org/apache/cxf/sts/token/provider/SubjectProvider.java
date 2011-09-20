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
name|provider
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|SubjectBean
import|;
end_import

begin_comment
comment|/**  * An interface that allows a pluggable way of obtaining a SAML SubjectBean used to populate the  * Subject of a SAML Assertion.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SubjectProvider
block|{
comment|/**      * Get a SubjectBean object.      */
name|SubjectBean
name|getSubject
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|,
name|Document
name|doc
parameter_list|,
name|byte
index|[]
name|secret
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

