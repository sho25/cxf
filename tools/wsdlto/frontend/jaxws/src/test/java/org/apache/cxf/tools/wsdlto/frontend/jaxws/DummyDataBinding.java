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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|DefaultValueWriter
import|;
end_import

begin_class
specifier|public
class|class
name|DummyDataBinding
implements|implements
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|core
operator|.
name|DataBindingProfile
block|{
specifier|public
name|void
name|generate
parameter_list|(
name|ToolContext
name|context
parameter_list|)
throws|throws
name|ToolException
block|{     }
specifier|public
name|void
name|initialize
parameter_list|(
name|ToolContext
name|c
parameter_list|)
throws|throws
name|ToolException
block|{      }
specifier|public
name|String
name|getType
parameter_list|(
name|QName
name|qn
parameter_list|,
name|boolean
name|element
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getWrappedElementType
parameter_list|(
name|QName
name|parent
parameter_list|,
name|QName
name|qn
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|DefaultValueWriter
name|createDefaultValueWriter
parameter_list|(
name|QName
name|qn
parameter_list|,
name|boolean
name|element
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|DefaultValueWriter
name|createDefaultValueWriterForWrappedElement
parameter_list|(
name|QName
name|wrapperElement
parameter_list|,
name|QName
name|qn
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

