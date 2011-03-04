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
name|configuration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfiguredBeanLocator
block|{
comment|/**      * Gets the names of all the configured beans of the specific type.  Does      * not cause them to be loaded.      * @param type      * @return List of all the bean names for the given type      */
name|List
argument_list|<
name|String
argument_list|>
name|getBeanNamesOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Gets all the configured beans of the specific types.  Causes them      * all to be loaded.       * @param type      * @return The collection of all the configured beans of the given type      */
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|getBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Iterates through the beans of the given type, calling the listener      * to determine if it should be loaded or not.       * @param type      * @param listener      * @return true if beans of the type were loaded      */
parameter_list|<
name|T
parameter_list|>
name|boolean
name|loadBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|BeanLoaderListener
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|)
function_decl|;
comment|/**      * For supporting "legacy" config, checks the configured bean to see if      * it has a property configured with the given name/value.  Mostly used       * for supporting things configured with "activationNamespaces" set.       * @param beanName      * @param propertyName      * @param value      * @return true if the bean has the given property/value      */
name|boolean
name|hasConfiguredPropertyValue
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
specifier|public
interface|interface
name|BeanLoaderListener
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**          * Return true to have the loader go ahead and load the bean.  If false,           * the loader will just skip to the next bean          * @param name          * @param type          * @return true if the bean should be loaded           */
name|boolean
name|loadBean
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**          * Return true if the bean that was loaded meets the requirements at          * which point, the loader will stop loading additional beans of the          * given type          * @param name          * @param bean          * @return true if the bean meets the requirements of the listener          */
name|boolean
name|beanLoaded
parameter_list|(
name|String
name|name
parameter_list|,
name|T
name|bean
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

