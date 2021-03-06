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
name|model
operator|.
name|wadl
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
comment|/**  * Can be used to document resource classes and methods  *  * See<a href="http://www.w3.org/Submission/wadl/#x3-80002.3">WADL Documentation</a>.  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|PARAMETER
block|,
name|ElementType
operator|.
name|FIELD
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
name|Description
block|{
comment|/**      * This value, if set, will be set as WADL doc content      */
name|String
name|value
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Maps to WADL doc/@xml:lang attribute      **/
name|String
name|lang
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Maps to WADL doc/@title attribute      **/
name|String
name|title
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * This uri, if set, will be used to retrieve      * the content which will be set as WADL doc content      */
name|String
name|docuri
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Target of this description, see {@link DocTarget}      */
name|String
name|target
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

