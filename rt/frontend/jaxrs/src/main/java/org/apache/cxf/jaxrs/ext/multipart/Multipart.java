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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Annotate a JAX-RS function parameter to receive data from a multipart 'part'.  **/
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|PARAMETER
block|,
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
specifier|public
annotation_defn|@interface
name|Multipart
block|{
comment|/**      * The name of the MIME part to map to this parameter. The default is      * the unnamed default part.      **/
name|String
name|value
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Select the part by MIME type. The default is to match any MIME type.      */
name|String
name|type
parameter_list|()
default|default
literal|"*/*"
function_decl|;
comment|/**      * How to handle a missing part. By default, if no part matches,      * the {@link org.apache.cxf.jaxrs.provider.MultipartProvider}       * throws a {@link javax.ws.rs.WebApplicationException}      * with status 400. If this option is set to<strong>false</strong>,      * the parameter is set to<strong>null</strong>.      */
name|boolean
name|errorIfMissing
parameter_list|()
default|default
literal|true
function_decl|;
block|}
end_annotation_defn

end_unit

