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
name|spring
operator|.
name|boot
operator|.
name|autoconfigure
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintViolationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

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
name|annotation
operator|.
name|Autowired
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|validation
operator|.
name|beanvalidation
operator|.
name|MethodValidationPostProcessor
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|CxfPropertiesTest
operator|.
name|Config
operator|.
name|class
block|}
argument_list|)
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|CxfPropertiesTest
extends|extends
name|Assert
block|{
annotation|@
name|Configuration
specifier|public
specifier|static
class|class
name|Config
block|{
annotation|@
name|Bean
specifier|public
name|MethodValidationPostProcessor
name|methodValidationPostProcessor
parameter_list|()
block|{
return|return
operator|new
name|MethodValidationPostProcessor
argument_list|()
return|;
block|}
annotation|@
name|Bean
specifier|public
name|CxfProperties
name|cxfProperties
parameter_list|()
block|{
return|return
operator|new
name|CxfProperties
argument_list|()
return|;
block|}
block|}
annotation|@
name|Autowired
specifier|private
name|CxfProperties
name|cxfproperties
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|throwsViolationExceptionWhenIsNull
parameter_list|()
block|{
name|doTestInvalidPath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|throwsViolationExceptionWhenPathIsEmpty
parameter_list|()
block|{
name|doTestInvalidPath
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|throwsViolationExceptionWhenHasNoSlash
parameter_list|()
block|{
name|doTestInvalidPath
argument_list|(
literal|"invalid"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestInvalidPath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|cxfproperties
operator|.
name|setPath
argument_list|(
name|value
argument_list|)
expr_stmt|;
try|try
block|{
name|cxfproperties
operator|.
name|getPath
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"ConstraintViolationException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConstraintViolationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e
operator|.
name|getConstraintViolations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|noViolationExceptionWhenPathValid
parameter_list|()
block|{
name|cxfproperties
operator|.
name|setPath
argument_list|(
literal|"/valid"
argument_list|)
expr_stmt|;
name|cxfproperties
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

