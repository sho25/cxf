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
name|common
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * Utility class for checking well-known system properties  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SystemUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SPRING_VALIDATION_MODE
init|=
literal|"org.apache.cxf.spring.validation.mode"
decl_stmt|;
specifier|private
name|SystemUtils
parameter_list|()
block|{              }
comment|/**      * Gets org.apache.cxf.spring.validation.mode property value if available       * @return Spring validation mode      */
specifier|public
specifier|static
name|String
name|getSpringValidationMode
parameter_list|()
block|{
name|String
name|mode
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SPRING_VALIDATION_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|==
literal|null
condition|)
block|{
name|mode
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring.validation.mode"
argument_list|)
expr_stmt|;
block|}
return|return
name|mode
return|;
block|}
block|}
end_class

end_unit

