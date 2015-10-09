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
name|annotations
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
name|Documented
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
name|Inherited
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
comment|/**  * Specifies a property to record for the endpoint  */
end_comment

begin_annotation_defn
annotation|@
name|Documented
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
block|{
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
annotation|@
name|Inherited
specifier|public
annotation_defn|@interface
name|EndpointProperty
block|{
comment|/**      * The value(s) of the property      * @return the value of the property      */
name|String
index|[]
name|value
argument_list|()
expr|default
block|{ }
expr_stmt|;
comment|/**      * The key to record the property      * @return the key for the property      */
name|String
name|key
parameter_list|()
function_decl|;
comment|/**      * Reference to a named bean that is looked up from the      * configuration associated with the application.       */
name|String
name|ref
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * The class for the property. If "ref" is specified,      * this class is used to cast the looked up reference      * to make sure the Object is of the correct type.       *       * If ref is not set and value is not set, this class      * is used to create a bean. The class must have either       * a default constructor, a constructor that takes an      * org.apache.cxf.endpoint.Endpoint, or a constructor      * that takes a org.apache.cxf.endpoint.Endpoint and       * an org.apache.cxf.Bus.      */
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|()
default|default
name|Object
operator|.
name|class
function_decl|;
block|}
end_annotation_defn

end_unit

