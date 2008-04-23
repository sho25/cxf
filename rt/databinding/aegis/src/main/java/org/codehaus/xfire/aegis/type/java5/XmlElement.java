begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|codehaus
operator|.
name|xfire
operator|.
name|aegis
operator|.
name|type
operator|.
name|java5
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|type
operator|.
name|Type
import|;
end_import

begin_comment
comment|/**  * Provided only for backward compatability with XFire clients.  *  * @deprecated use org.apache.cxf.aegis.type.java5.XmlElement  */
end_comment

begin_annotation_defn
annotation|@
name|Deprecated
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|METHOD
argument_list|)
specifier|public
annotation_defn|@interface
name|XmlElement
block|{
name|Class
name|type
parameter_list|()
default|default
name|Type
operator|.
name|class
function_decl|;
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
name|String
name|namespace
parameter_list|()
default|default
literal|""
function_decl|;
name|boolean
name|nillable
parameter_list|()
default|default
literal|true
function_decl|;
comment|/**      * Set to "0" to make the property optional, "1" for required      */
name|String
name|minOccurs
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

