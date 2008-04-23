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
name|profile
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|EndpointCreationLoop
block|{
specifier|private
name|GenericApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|EndpointCreationLoop
parameter_list|()
block|{     }
specifier|private
name|void
name|readBeans
parameter_list|(
name|Resource
name|beanResource
parameter_list|)
block|{
name|XmlBeanDefinitionReader
name|reader
init|=
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
name|reader
operator|.
name|loadBeanDefinitions
argument_list|(
name|beanResource
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|iteration
parameter_list|()
block|{
name|applicationContext
operator|=
operator|new
name|GenericApplicationContext
argument_list|()
expr_stmt|;
name|readBeans
argument_list|(
operator|new
name|ClassPathResource
argument_list|(
literal|"extrajaxbclass.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|applicationContext
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * @param args      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|EndpointCreationLoop
name|ecl
init|=
operator|new
name|EndpointCreationLoop
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|count
condition|;
name|x
operator|++
control|)
block|{
name|ecl
operator|.
name|iteration
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

