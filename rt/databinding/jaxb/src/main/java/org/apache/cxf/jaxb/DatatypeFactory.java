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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|DatatypeConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|Duration
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_comment
comment|/**  * Utility class to construct javax.xml.datatype.Duration objects.  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DatatypeFactory
block|{
specifier|public
specifier|static
specifier|final
name|Duration
name|PT0S
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|DatatypeFactory
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|PT0S
operator|=
name|createDuration
argument_list|(
literal|"PT0S"
argument_list|)
expr_stmt|;
block|}
comment|/**      * prevents instantiation      *      */
specifier|private
name|DatatypeFactory
parameter_list|()
block|{      }
specifier|public
specifier|static
name|Duration
name|createDuration
parameter_list|(
name|String
name|s
parameter_list|)
block|{
try|try
block|{
return|return
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
name|s
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DatatypeConfigurationException
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"DATATYPE_FACTORY_INSTANTIATION_EXC"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

