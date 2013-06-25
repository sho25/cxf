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
name|service
operator|.
name|stax
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|databinding
operator|.
name|stax
operator|.
name|StaxDataBinding
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
name|databinding
operator|.
name|stax
operator|.
name|StaxDataBindingFeature
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
name|databinding
operator|.
name|stax
operator|.
name|XMLStreamWriterCallback
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|interceptor
operator|.
name|Fault
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
name|staxutils
operator|.
name|FragmentStreamReader
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
name|test
operator|.
name|AbstractCXFTest
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
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
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

begin_class
specifier|public
class|class
name|StaxDatabindingTest
extends|extends
name|AbstractCXFTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCallback
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"local://foo"
decl_stmt|;
name|ServerFactoryBean
name|sf
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|CallbackService
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setDataBinding
argument_list|(
operator|new
name|StaxDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxDataBindingFeature
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"req.xml"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//bleh"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"local://foo"
decl_stmt|;
name|ServerFactoryBean
name|sf
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|CopyService
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setDataBinding
argument_list|(
operator|new
name|StaxDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxDataBindingFeature
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"req.xml"
argument_list|)
decl_stmt|;
comment|//DOMUtils.writeXml(res, System.out);
name|addNamespace
argument_list|(
literal|"a"
argument_list|,
literal|"http://stax.service.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//a:bleh"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|CallbackService
block|{
specifier|public
name|XMLStreamWriterCallback
name|invoke
parameter_list|(
specifier|final
name|XMLStreamReader
name|reader
parameter_list|)
block|{
try|try
block|{
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|XMLStreamWriterCallback
argument_list|()
block|{
specifier|public
name|void
name|write
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|Fault
throws|,
name|XMLStreamException
block|{
name|writer
operator|.
name|writeEmptyElement
argument_list|(
literal|"bleh"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CopyService
block|{
specifier|public
name|XMLStreamReader
name|invoke
parameter_list|(
specifier|final
name|XMLStreamReader
name|reader
parameter_list|)
block|{
return|return
operator|new
name|FragmentStreamReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

