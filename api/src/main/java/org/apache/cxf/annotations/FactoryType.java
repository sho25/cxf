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
comment|/**  * Defines the factory used for the service.  *   * Either use the factoryClass attribute to define your own   * factory or use one of the "value" convenience enums.  */
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
specifier|public
annotation_defn|@interface
name|FactoryType
block|{
name|Type
name|value
parameter_list|()
default|default
name|Type
operator|.
name|Singleton
function_decl|;
name|String
index|[]
name|args
argument_list|()
expr|default
block|{ }
expr_stmt|;
comment|/**      * The class for the factory.  It MUST have a constructor that takes      * two arguments:      *    1) The Class for the service      *    2) String[] of the args from above       */
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
parameter_list|()
default|default
name|DEFAULT
operator|.
name|class
function_decl|;
enum|enum
name|Type
block|{
name|Singleton
block|,
name|Session
block|,
name|Pooled
block|,
comment|//args[0] is the size of the pool
name|PerRequest
block|,
name|Spring
block|,
comment|//args[0] is the Spring bean name
block|}
empty_stmt|;
specifier|static
specifier|final
class|class
name|DEFAULT
block|{ }
block|}
end_annotation_defn

end_unit

